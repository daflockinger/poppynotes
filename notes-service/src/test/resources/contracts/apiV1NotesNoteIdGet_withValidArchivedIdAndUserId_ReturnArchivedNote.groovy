package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes/aexistingNoteId'
		headers { header('userId','1') }
	}
	response {
		status 200
		body([
			id: $(regex('.+')),
			title: 'a1latest',
			lastEdit: $(regex('.+')),
			archived: true,
			content: $(regex('.+'))
		])
		headers { contentType('application/json') }
	}
}