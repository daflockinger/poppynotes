package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'POST'
		url '/api/v1/notes'
		body([
			title: 'new note',
			lastEdit: '2012-12-12T12:12:12Z',
			userId: 1,
			archived: false,
			content: 'some text'
			])
		headers {
			header('userId','1')
			contentType('application/json')
		}
	}
	response {
		status 400
		body([
			code: null,
			message: $(regex('.+'))
		])
		headers {
			contentType('application/json')
		}
	}
}