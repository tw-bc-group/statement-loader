all: build

build:
	./gradlew build

image: build
	docker build -t tw-blockchain/reconciliation-bridge-http-mock ./mock/bridge-http
	docker build -t tw-blockchain/reconciliation-loader .