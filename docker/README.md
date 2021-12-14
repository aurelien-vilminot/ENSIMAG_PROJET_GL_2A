# Docker image corresponding to ensimag environment

Docker is one of the possibilities if you want to work on the project with your personal computer. More informations are available on the [gitlab](https://projet-gl.pages.ensimag.fr/environnement/machine_perso/).

This docker image is proposing an ensimag like environment that can be used to recompile and run JDeca, use ima for your tests and recompile it if needed.

You can either use an existing image available on the ensimag gitlab or rebuild it locally if you want to customize it.

In both cases, when you start the container, you will be in a bash shell on a ubuntu environment in your GL directory hosted by your local operating system. It means that all the project files are both visible from the docker environment and the local operating system

In the docker environment, the user is "gl". This user is sudoer, so that you can manually install new packages if needed (or rebuild a new image adding this package in the Dockerfile for full automation). The "gl" user password (for the sudo command) is "gl".... (no flame !). This can be seen in the Dockerfile.

## Using the existing image

An existing image is available on the ensimag gitlab in the dockergl project. You first have to login on the gitlab docker registry :

```bash
$ docker login gitlab.ensimag.fr:5050
```

You can then create a container (lightweight virtual machine) based on the remote image. The container is named ```projetgl```

```bash
$  docker create --interactive --tty -v <your GL home dir absolute path>:/home/gl/Projet_GL --name projetgl gitlab.ensimag.fr:5050/reigniep/dockergl
```

 Once the container has been created, you can start it with the following command : 

```bash
$ docker start -a -i projetgl
```

## Building and running the docker image

To build the image :

```bash
$ docker build -t projetgl .
```

Once the image has been built, (it may take some time.....), a new container (lightweight virtual machine) can be created with the following command :

```bash
$ docker create --interactive --tty -v <your GL home dir absolute path>:/home/gl/Projet_GL --name projetgl projetgl
```

This command is in the `new_container` shell script (for linux and mac users) for convenience.  Once the container has been created, you can start it with the following command : 

```bash
$ docker start -a -i projetgl
```

## Dockerfile.ssh

This Docker image build file allows you to set up an image that does not trigger a shell but ``sshd``. This allows a remote access to the container via ssh and this can be useful to control the compilation from an IDE (see [gitlab](https://projet-gl.pages.ensimag.fr/environnement/machine_perso/) for more details). It can be useful also if you need to open simultaneous access to the container (which is not possible in the previous section where the container is a "just" a bash).

* Building the image:

```bash
$ docker build -f Dockerfile.ssh -t projetgl_ssh .
```

* Creating the container (the interactive options are removed : the container is no more an interactive shell but a sshd server):

```bash
$ docker create -v <your GL home dir absolute path>:/home/gl/Projet_GL --name projetgl_ssh projetgl_ssh
```

* Launching the sshd container:

```bash
$ docker start projetgl_ssh
```
* Stopping the sshd container:

```bash
$ docker stop projetgl_ssh
```
* Find the IP address of the container 

```bash
$ docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' projetgl_ssh
```

* Connecting to the container: the user is still gl and the password is gl also:

```bash
$ ssh gl@<ip address>
```




