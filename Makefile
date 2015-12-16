#
# Makefile to manage this project's development environment.
#

TAG=luontola/retrolambda-dev
NAME=wrapping-retrolambda-shell

# Directory that this Makefile is in
mkfile_path := $(abspath $(lastword $(MAKEFILE_LIST)))
current_path := $(dir $(mkfile_path))

# Builds the development docker file
docker-build:
	docker build --tag=$(TAG) ./dev

# Clean this docker image
docker-clean:
	-docker rmi $(TAG)

# Start a development shell
shell:
	mkdir -p ~/.m2
	docker run --rm \
		--name=$(NAME) \
		-P=true \
		-v ~/.m2/repository:/root/.m2/repository \
		-v ~/.m2/settings.xml:/root/.m2/settings.xml \
		-v ~/.ssh:/root/.ssh \
		-v ~/.gitconfig:/root/.gitconfig \
		-v $(current_path):/project/retrolambda \
		-v $(abspath $(current_path)/../retrolambda.pages):/project/retrolambda.pages \
		-it $(TAG) /bin/bash
# FIXME: git/ssh complains about wrong permissions on ssh config files, and chown doesn't work on them (docker-machine on OS X)
# TODO: mount ~/.gnupg

# Attach a new terminal to the already running shell
shell-attach:
	docker exec -it -u=$(USER) $(NAME) /bin/bash
