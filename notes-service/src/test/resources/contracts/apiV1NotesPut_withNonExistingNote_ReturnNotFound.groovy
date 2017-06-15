package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'PUT'
		url '/api/v1/notes'
		body([
			id: 'nonExista',
			title: 'new note',
			lastEdit: '2012-12-12T12:12:12Z',
			pinned: false,
			archived: false,
			content: 'some text',
			userId: 1
			])
		headers {
			header('userId','1')
			contentType('application/json')
		}
	}
	response {
		status 404
		body([
			code: null,
			message: $(regex('.+'))
		])
		headers {
			contentType('application/json')
		}
	}
}