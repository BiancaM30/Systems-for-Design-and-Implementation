package swim.services;


import swim.model.hbm.DeskUser;
import swim.model.SwimmingEvent;
import swim.model.Contestant;
import swim.model.Registration;


import java.util.List;


public interface ISwimServices {
    void login(DeskUser user, ISwimObserver client) throws Exception;

    void logout(DeskUser user, ISwimObserver client) throws Exception;

    DeskUser getDeskUserByEmail(String email);

    int getNumberOfContestants(SwimmingEvent event) throws Exception;

    void addRegistration(Registration registration) throws Exception;

    List<Contestant> findAllContestants() throws Exception;

    List<SwimmingEvent> findAllSwimmingEvents() throws Exception;

    List<Contestant> getSubmittedContestants(SwimmingEvent event) throws Exception;

    List<SwimmingEvent> getSubmittedEvents(Contestant contestant) throws Exception;
}