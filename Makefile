all: build

build:
	./gradlew build

image: build
	docker build -t tw-blockchain/reconciliation-bridge-http-mock ./mock/bridge-http
	docker build -t tw-blockchain/reconciliation-loader .

publish:
	docker image tag tw-blockchain/reconciliation-bridge-http-mock localhost:5000/reconciliation-bridge-http-mock
	docker push localhost:5000/reconciliation-bridge-http-mock
	docker image tag tw-blockchain/reconciliation-loader localhost:5000/reconciliation-loader
	docker push localhost:5000/reconciliation-loader