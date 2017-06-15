package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes/existingNoteId'
		headers {
			header('userId','1234')
		}
	}
	response {
		status 403
		body([
			   code: null,
			   message: $(regex('[a-zA-Z]+'))
		])
		headers {
			contentType('application/json')
		}
	}
}