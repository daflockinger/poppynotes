package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'DELETE'
		url '/api/v1/notes/nonExista'
		headers { header('userId','1') }
	}
	response {
		status 404
		body([
			code: null,
			message: $(regex('.+'))
		])
		headers { contentType('application/json') }
	}
}