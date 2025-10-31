package net.miarma.api.backlib.core.entities;

import java.time.LocalDateTime;

import io.vertx.sqlclient.Row;
import net.miarma.api.backlib.Constants.CoreUserGlobalStatus;
import net.miarma.api.backlib.Constants.CoreUserRole;
import net.miarma.api.backlib.annotations.APIDontReturn;
import net.miarma.api.backlib.annotations.Table;
import net.miarma.api.backlib.db.AbstractEntity;
import net.miarma.api.backlib.interfaces.IUser;

@Table("users")
public class UserEntity extends AbstractEntity implements IUser {
    private Integer user_id;
    private String user_name;
    private String email;
    private String display_name;
    @APIDontReturn
    private String password;
    private String avatar;
    private CoreUserGlobalStatus global_status;
    private CoreUserRole role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public UserEntity() { }
    public UserEntity(Row row) { super(row); }
    
    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public String getUser_name() { return user_name; }
    public void setUser_name(String user_name) { this.user_name = user_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplay_name() { return display_name; }
    public void setDisplay_name(String display_name) { this.display_name = display_name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public CoreUserGlobalStatus getGlobal_status() { return global_status; }
    public void setGlobal_status(CoreUserGlobalStatus global_status) { this.global_status = global_status; }
    public CoreUserRole getGlobal_role() { return role; }
    public void setGlobal_role(CoreUserRole role) { this.role = role; }
    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }
    
    public static UserEntity from(IUser user) {
    	UserEntity entity = new UserEntity();
        entity.setUser_id(user.getUser_id());
        entity.setUser_name(user.getUser_name());
        entity.setDisplay_name(user.getDisplay_name());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setAvatar(user.getAvatar());
        entity.setGlobal_status(user.getGlobal_status());
        entity.setGlobal_role(user.getGlobal_role());
        return entity;
    }  
}