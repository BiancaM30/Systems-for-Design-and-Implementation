package swim.persistance.interfaces;

import swim.model.Contestant;
import swim.model.Registration;
import swim.model.SwimmingEvent;
import swim.model.Tuple;

import java.util.List;


public interface RegistrationRepository extends Repository<Registration, Tuple<Contestant, SwimmingEvent>> {

    Integer nrOfContestants(SwimmingEvent swimmingEvent);

    List<Contestant> getSubmittedContestants(SwimmingEvent swimmingEvent);

    List<SwimmingEvent> getRegisteredEvents(Contestant contestant);
}
