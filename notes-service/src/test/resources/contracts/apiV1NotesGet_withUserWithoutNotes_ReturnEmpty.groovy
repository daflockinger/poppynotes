package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes'
		headers { header('userId','38748') }
	}
	response {
		status 200
		body([])
		headers { contentType('application/json') }
	}
}