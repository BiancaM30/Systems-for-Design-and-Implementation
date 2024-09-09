package swim.persistance.interfaces;

import swim.model.hbm.DeskUser;

public interface DeskUserRepository extends Repository<DeskUser, Integer> {
    DeskUser findByEmail(String email);
}
