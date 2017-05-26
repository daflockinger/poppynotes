package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'POST'
		url '/api/v1/user-checks/auth'
		body(
			   authEmail:'nonexista@gmail.com'
		)
		headers {
			contentType('application/json')
		}
	}
	response {
		status 404
		body([
			   code: null,
			   message: 'User not found',
			   fields: null
		])
		headers {
			contentType('application/json')
		}
	}
}