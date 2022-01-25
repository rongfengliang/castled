---
sidebar_position: 3
---

# On Kubernetes(Beta)

Castled supports launching it services as a kubernetes deployment. This provides the flexibility of scaling the services horizontally based on requirement.
In this doc we cover the steps to setup a basic deployment on various Kubernetes cluster environments.

Ideally in a production environment you would want to setup autoscaling for your cluster. But this needs an approach that is specific to the cloud provider and we won't be covering that here.

:::note
At this point we cover deployment on AWS EKS only. We will be soon updating the docs as we test out deployment of Castled services on various Kubernetes cluster environments.
:::

At a high level there are 5 major services that are part of Castled deployment.

- **app** - Core service that handles all connectors, scheduling etc.
- **db** - Internal database used by Castled services. This is a mysql database.
- **kafka** - Internal messaging service used by app service.
- **zookeeper** - Coordinator for kafka.
- **redis** - Used as a low latency cache by app service.
- **webapp** - Web server for Castled UI.

You can scale the core app or webapp service in a cluster independently based on the load.

---

## Cluster Setup

You can setup a Kubernetes cluster locally(minikube, docker, kind etc) or on any of the cloud providers (AWS EKS, GKE)

### AWS EKS

[AWS EKS](https://aws.amazon.com/eks/) is a managed kubernetes cluster offering by Amazon. [eksctl](https://eksctl.io/) is a very convenient tool for creating and
managing EKS cluster. You should be able to setup a cluster in less than 20 mins with eksctl.

Following are some useful pointers to get started with a EKS cluster

1. Installation instructions for eksctl can be found [here](https://github.com/weaveworks/eksctl)
2. Quick commands for creating and managing a cluster can be found [here](https://eksctl.io/usage/creating-and-managing-clusters/)

### GKE

Coming soon...

### Local Cluster

Coming soon...

---

## Kubectl Setup

### AWS

The kubectl command line tool lets you control Kubernetes clusters from your local workstation.

- [Install](https://kubernetes.io/docs/tasks/tools/) kubectl.
- [Configure AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html) with `aws_access_key_id` and `aws_secret_access_key` if you haven't done as part of eksctl setup.
- `eksctl utils write-kubeconfig --cluster=<eks_cluster_name>` will update the kubeconfig with the context of your eks cluster.
- `kubectl config get-contexts` will list all cluster contexts.
- `kubectl use-context <eks_context>` will setup kubectl to work with your eks cluster.

:::tip
[kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/) is a handy reference to commonly used kubectl commands
:::

---

## Kubernetes Deployment

Castled uses [kustomization](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) to manage Kubernetes objects.

- All the basic deployment files are located at `kube/resources/`
- Any override based on a specific deployment environment will be located at `kube/overlays/`, i.e. every deployment environment will have its own directory here.
- If you are tweaking the deployment to your specific needs, `kube/overlays/` is where those changes resides.
- To just view the deployment objects(without applying them), use command
  ```
  kubectl kustomize <path_to_kustomization_dir>
  ```

* Finally to deploy Castled services

  ```jsx title="Kubernetes Deployment"
  git clone https://github.com/castledio/castled.git
  cd castled
  kubectl apply -k kube/overlays/community

  # Wait for a few mins to so that all the pods starts running
  # To see all the pods and their states
  kubectl get pods
  ```

  Once the pods are up and running you can setup port-forwarding to access the Castled UI.

  ```jsx title="Setup port-forwarding"
  # To see all the services
  kubectl get svc

  # You should see the castled-webapp-service running listening on port 80.
  kubectl port-forward svc/castled-webapp-service 3000:80
  ```

  Castled UI should now be accessible at [http://localhost:3000](http://localhost:3000)

  :::note
  Please reach out to us at hello@castled.io if you have any questions or trouble setting up.
  :::
