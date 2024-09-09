package swim.server;

import swim.model.*;
import swim.model.hbm.DeskUser;
import swim.persistance.interfaces.ContestantRepository;
import swim.persistance.interfaces.DeskUserRepository;
import swim.persistance.interfaces.RegistrationRepository;
import swim.persistance.interfaces.SwimmingEventRepository;
import swim.services.ISwimObserver;
import swim.services.ISwimServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SwimServicesImpl implements ISwimServices {

    private ContestantRepository contestantRepo;
    private DeskUserRepository deskUserRepo;
    private RegistrationRepository registrationRepo;
    private SwimmingEventRepository swimmingEventRepo;

    private Map<String, ISwimObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public SwimServicesImpl(ContestantRepository contestantRepo, DeskUserRepository deskUserRepo, RegistrationRepository registrationRepo, SwimmingEventRepository swimmingEventRepo) {
        this.contestantRepo = contestantRepo;
        this.deskUserRepo = deskUserRepo;
        this.registrationRepo = registrationRepo;
        this.swimmingEventRepo = swimmingEventRepo;
        loggedClients = new ConcurrentHashMap<>();
    }


    public void login(DeskUser deskUser, ISwimObserver client) throws Exception {
        DeskUser userR=deskUserRepo.findByEmail(deskUser.getEmail());
        if (userR!=null && userR.getPassword().equals(deskUser.getPassword())){
            if(loggedClients.get(userR.getEmail())!=null)
                throw new Exception("User already logged in.");
            loggedClients.put(userR.getEmail(), client);
        }else
            throw new Exception("Authentication failed.");

        Iterable<DeskUser> itrUsers = deskUserRepo.findAll();
        List<DeskUser> users = new ArrayList<>();
        itrUsers.forEach(users::add);

        DeskUser resultUser = null;
        for (DeskUser user : users) {
            System.out.println(user);
            if (user.getEmail().equals(deskUser.getEmail()) && user.getPassword().equals(deskUser.getPassword()))
                resultUser = user;

        }
        if (resultUser == null) {
            throw new Exception("Invalid login!");
        } else {
            loggedClients.put(resultUser.getEmail(), client);
        }
    }

    @Override
    public void logout(DeskUser user, ISwimObserver client) throws Exception {
        ISwimObserver localClient = loggedClients.remove(user.getEmail());
        if (localClient == null)
            throw new Exception("User " + user.getID() + " is not logged in.");
    }

    @Override
    public DeskUser getDeskUserByEmail(String email) {
        DeskUser user = deskUserRepo.findByEmail(email);
        return user;
    }


    public void logout(DeskUser user) {
        loggedClients.remove(user.getEmail());
    }

    public List<SwimmingEvent> findAllSwimmingEvents() {
        System.out.println("Am ajuns in Impl");
        Iterable<SwimmingEvent> itrEvents = swimmingEventRepo.findAll();
        List<SwimmingEvent> events = new ArrayList<>();
        itrEvents.forEach(events::add);

        return events;
    }

    public int getNumberOfContestants(SwimmingEvent event) {
        return registrationRepo.nrOfContestants(event);
    }

    public List<Contestant> findAllContestants() {
        Iterable<Contestant> itrContestants = contestantRepo.findAll();
        List<Contestant> contestants = new ArrayList<>();
        itrContestants.forEach(contestants::add);

        return contestants;
    }

    public List<Contestant> getSubmittedContestants(SwimmingEvent event) {
        return registrationRepo.getSubmittedContestants(event);
    }

    public List<SwimmingEvent> getSubmittedEvents(Contestant contestant) {
        return registrationRepo.getRegisteredEvents(contestant);
    }

    public synchronized void addRegistration(Registration registration) throws Exception {
        registrationRepo.add(registration);
        notifyAddedRegistration(registration);
    }

    private void notifyAddedRegistration(Registration registration) throws Exception {
        registrationRepo.add(registration);

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

        for (ISwimObserver swimClient : loggedClients.values()) {
            if (swimClient != null) {
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying client " + swimClient);
                        swimClient.registrationAdded(registration);
                    } catch (Exception e) {
                        System.err.println("Error notifying client " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }
}

