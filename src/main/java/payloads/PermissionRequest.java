
package payloads;

import javax.validation.constraints.NotNull;

public class PermissionRequest {

    @NotNull
    private Integer role_id;

    @NotNull
    private Integer permission_id;

    @NotNull
    private Boolean create;

    @NotNull
    private Boolean read;

    @NotNull
    private Boolean update;

    @NotNull
    private Boolean delete;

    public PermissionRequest() {
    }

    public PermissionRequest(Integer role_id, Integer permission_id, Boolean create, Boolean read, Boolean update, Boolean delete) {
        this.role_id = role_id;
        this.permission_id = permission_id;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public Integer getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(Integer permission_id) {
        this.permission_id = permission_id;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}