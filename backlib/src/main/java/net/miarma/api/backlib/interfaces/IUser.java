package net.miarma.api.backlib.interfaces;

import java.time.LocalDateTime;

import net.miarma.api.backlib.Constants.CoreUserGlobalStatus;
import net.miarma.api.backlib.Constants.CoreUserRole;

public interface IUser {
	Integer getUser_id();
    String getUser_name();
    String getEmail();
    String getDisplay_name();
    String getPassword();
    String getAvatar();
    CoreUserGlobalStatus getGlobal_status();
    CoreUserRole getGlobal_role();
    LocalDateTime getCreated_at();
    default LocalDateTime getUpdated_at() {
        return null;
    }
}
