package org.sandbox.quarkus.k8s.svc.discovery.fabric8.features;

import com.oracle.svm.core.annotate.AutomaticFeature;
import io.fabric8.kubernetes.api.model.AuthInfo;
import io.fabric8.kubernetes.api.model.AuthProviderConfig;
import io.fabric8.kubernetes.api.model.ClientIPConfig;
import io.fabric8.kubernetes.api.model.Cluster;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.Context;
import io.fabric8.kubernetes.api.model.ExecConfig;
import io.fabric8.kubernetes.api.model.ExecEnvVar;
import io.fabric8.kubernetes.api.model.Initializer;
import io.fabric8.kubernetes.api.model.Initializers;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ListMeta;
import io.fabric8.kubernetes.api.model.LoadBalancerStatus;
import io.fabric8.kubernetes.api.model.NamedAuthInfo;
import io.fabric8.kubernetes.api.model.NamedCluster;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.api.model.NamedExtension;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.NamespaceSpec;
import io.fabric8.kubernetes.api.model.NamespaceStatus;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.api.model.Preferences;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.api.model.SessionAffinityConfig;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.StatusCause;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import org.graalvm.nativeimage.hosted.Feature;
import org.sandbox.quarkus.k8s.svc.discovery.features.RutimeReflectionRegistrationSupport;

@AutomaticFeature
public class RuntimeReflectionRegistrationFeature extends RutimeReflectionRegistrationSupport implements Feature {

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        // authentication and configuration classes
        this.registerAll(Config.class);
        this.registerAll(NamedCluster.class);
        this.registerAll(NamedContext.class);
        this.registerAll(NamedExtension.class);
        this.registerAll(Preferences.class);
        this.registerAll(NamedAuthInfo.class);
        this.registerAll(Cluster.class);
        this.registerAll(Context.class);
        this.registerAll(AuthInfo.class);
        this.registerAll(AuthProviderConfig.class);
        this.registerAll(ExecConfig.class);
        this.registerAll(ExecEnvVar.class);
        this.registerAll(KubernetesDeserializer.class);
        // service discovery
        this.registerAll(ServiceList.class);
        this.registerAll(Service.class);
        this.registerAll(ListMeta.class);
        this.registerAll(ObjectMeta.class);
        this.registerAll(ServiceSpec.class);
        this.registerAll(ServiceStatus.class);
        this.registerAll(Initializers.class);
        this.registerAll(OwnerReference.class);
        this.registerAll(ServicePort.class);
        this.registerAll(SessionAffinityConfig.class);
        this.registerAll(LoadBalancerStatus.class);
        this.registerAll(Initializer.class);
        this.registerAll(Status.class);
        this.registerAll(IntOrString.class);
        this.registerAll(ClientIPConfig.class);
        this.registerAll(StatusDetails.class);
        this.registerAll(StatusCause.class);
        this.registerAll(IntOrString.Deserializer.class);
        this.registerAll(IntOrString.Serializer.class);
        // namespace query
        this.registerAll(NamespaceList.class);
        this.registerAll(Namespace.class);
        this.registerAll(NamespaceSpec.class);
        this.registerAll(NamespaceStatus.class);
    }

}
