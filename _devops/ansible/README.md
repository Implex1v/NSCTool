Ansible
===

For a first version we are using ansible to deploy the NSCTool to a remote server. Later on, we might switch to Kubernetes.

## Development

### Install

As a prerequisite, ansbile needs to be [installed](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html). 

* for Ubuntu based distributions:
```shell
sudo apt update
sudo apt install software-properties-common
sudo add-apt-repository --yes --update ppa:ansible/ansible
sudo apt install ansible
```
* for Manjaro run `sudo pacman -uS ansible`


To install all dependencies run
```shell
ansible-galaxy collection install -r _devops/ansible/requirements.yml
```

### Running a playbook localy

You need to copy the `_devops/ansible/inventory` file to `_devops/ansible/.inventory` and fill out the host.

Afterwards, you can run a playbook on your machine (from project root) by executing the following command:

```shell
ansible-playbook --key-file <keyfile> --extra-vars 'cr_base=<host>/nsctool' -i _devops/ansible/.inventory _devops/ansible/playbooks/update.yml
```

### Running on a server
To get the playbook running on the server, different dependencies need to be installed:

```shell
pip2 install docker
pip2 install docker-compose==1.26.2
```

