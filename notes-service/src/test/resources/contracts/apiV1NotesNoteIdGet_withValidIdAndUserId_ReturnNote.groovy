package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes/existingNoteId'
		headers { header('userId','1') }
	}
	response {
		status 200
		body([
			id: $(regex('.+')),
			title: '1latest',
			lastEdit: $(regex('.+')),
			archived: false,
			content: 'this is a secret message.'
		])
		headers { contentType('application/json') }
	}
}