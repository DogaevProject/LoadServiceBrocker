package ru.test.load.model;

import ru.test.load.serviceTests.ServiceBinding;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class LoadServiceBinding extends ServiceBinding {
    @Column(name = "USER_NAME", nullable = false)
    private String userName;
    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;
    @Column(name = "URI", nullable = false)
    private String uri;

    public LoadServiceBinding() {}

    public LoadServiceBinding(String serviceInstanceId, String serviceBindingId, String applicationId, String userName, String userPassword, String uri) {
        super(serviceInstanceId, serviceBindingId, applicationId);
        this.userName = userName;
        this.userPassword = userPassword;
        this.uri = uri;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getURI() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoadServiceBinding that = (LoadServiceBinding) o;
        return userName.equals(that.userName) &&
                userPassword.equals(that.userPassword) &&
                uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, userPassword, uri);
    }

    @Override
    public String toString() {
        return "LoadServiceBinding{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
