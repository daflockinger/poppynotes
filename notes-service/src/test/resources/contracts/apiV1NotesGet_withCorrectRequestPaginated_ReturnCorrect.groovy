package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes?page=1&items=2'
		headers {
			header('userId','1')
		}
	}
	response {
		status 200
		body([
		      [
				  id: $(regex('.+')),
				  title: '3-pinned-third',
				  lastEdit: $(regex('.+')),
				  pinned: true,
				  archived: null,
				  partContent: $(regex('[a-zA-Z]+'))
			  ],
			  [
				  id: $(regex('.+')),
				  title: '1latest',
				  lastEdit: $(regex('.+')),
				  pinned: false,
				  archived: null,
				  partContent: $(regex('[a-zA-Z]+'))
			  ]
		])
		headers {
			contentType('application/json')
		}
	}
}