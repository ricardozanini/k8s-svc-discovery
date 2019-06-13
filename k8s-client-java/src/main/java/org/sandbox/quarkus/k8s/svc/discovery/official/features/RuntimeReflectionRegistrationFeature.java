package org.sandbox.quarkus.k8s.svc.discovery.official.features;

import com.oracle.svm.core.annotate.AutomaticFeature;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.V1ClientIPConfig;
import io.kubernetes.client.models.V1Initializer;
import io.kubernetes.client.models.V1Initializers;
import io.kubernetes.client.models.V1ListMeta;
import io.kubernetes.client.models.V1LoadBalancerIngress;
import io.kubernetes.client.models.V1LoadBalancerStatus;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1OwnerReference;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.models.V1ServicePort;
import io.kubernetes.client.models.V1ServiceSpec;
import io.kubernetes.client.models.V1ServiceStatus;
import io.kubernetes.client.models.V1SessionAffinityConfig;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.models.V1StatusDetails;
import org.graalvm.nativeimage.hosted.Feature;
import org.sandbox.quarkus.k8s.svc.discovery.features.RutimeReflectionRegistrationSupport;

@AutomaticFeature
public class RuntimeReflectionRegistrationFeature extends RutimeReflectionRegistrationSupport implements Feature {

    public void beforeAnalysis(BeforeAnalysisAccess access) {
        this.registerAll(V1ServiceList.class);
        this.registerAll(V1ListMeta.class);
        this.registerAll(V1Service.class);
        this.registerAll(V1ObjectMeta.class);
        this.registerAll(V1ServiceSpec.class);
        this.registerAll(V1ServiceStatus.class);
        this.registerAll(V1ServicePort.class);
        this.registerAll(V1ClientIPConfig.class);
        this.registerAll(V1SessionAffinityConfig.class);
        this.registerAll(V1StatusDetails.class);
        this.registerAll(V1Status.class);
        this.registerAll(V1Initializer.class);
        this.registerAll(V1Initializers.class);
        this.registerAll(V1OwnerReference.class);
        this.registerAll(V1LoadBalancerStatus.class);
        this.registerAll(V1LoadBalancerIngress.class);
        this.registerAll(IntOrString.IntOrStringAdapter.class);
    }

}
