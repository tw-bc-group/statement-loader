all: build

build:
	./gradlew build

image: build
	docker build -t tw-blockchain/reconciliation-loader .

publish:
	docker image tag tw-blockchain/reconciliation-loader localhost:5000/reconciliation-loader
	docker push localhost:5000/reconciliation-loader