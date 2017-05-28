package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'DELETE'
		url '/api/v1/notes/existingNoteId'
	}
	response {
		status 400
	}
}