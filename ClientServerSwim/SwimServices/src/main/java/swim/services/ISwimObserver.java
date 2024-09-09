package swim.services;


import swim.model.Contestant;
import swim.model.Registration;

public interface ISwimObserver {
    void registrationAdded(Registration registration) throws Exception;
}
