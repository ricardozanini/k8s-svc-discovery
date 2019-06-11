package org.sandbox.quarkus.k8s.svc.discovery.domain;

import java.util.Map;
import java.util.Objects;

public class ServiceInfo {

    private String url;
    private String name;
    private Map<String, String> labels;

    public ServiceInfo() {}

    public ServiceInfo(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    @Override
    public int hashCode() {
        return Objects.hash(labels, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceInfo other = (ServiceInfo) obj;
        return Objects.equals(labels, other.labels) && Objects.equals(url, other.url);
    }

    @Override
    public String toString() {
        return "ServiceInfo [url=" + url + ", labels=" + labels + "]";
    }

}
