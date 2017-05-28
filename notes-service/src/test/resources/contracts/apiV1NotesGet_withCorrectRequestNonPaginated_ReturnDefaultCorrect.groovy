package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/api/v1/notes'
		headers {
			header('userId','1')
		}
	}
	response {
		status 200
		body([
			  [
				  id: $(regex('.+')),
				  title: '2-pinned-second',
				  lastEdit: $(regex('.+')),
				  pinned: true,
				  archived: null,
				  partContent: $(regex('.+'))
			  ],
			  [
				  id: $(regex('.+')),
				  title: '3-pinned-third',
				  lastEdit: $(regex('.+')),
				  pinned: true,
				  archived: null,
				  partContent: $(regex('.+'))
			  ],
			  [
				  id: $(regex('.+')),
				  title: '1latest',
				  lastEdit: $(regex('.+')),
				  pinned: false,
				  archived: null,
				  partContent: $(regex('.+'))
			  ],
			  [
				  id: $(regex('.+')),
				  title: '2second',
				  lastEdit: $(regex('.+')),
				  pinned: false,
				  archived: null,
				  partContent: $(regex('.+'))
			  ]
		])
		headers {
			contentType('application/json')
		}
	}
}