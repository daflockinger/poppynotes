package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes'
	}
	response {
		status 400
		body([
		])
	}
}