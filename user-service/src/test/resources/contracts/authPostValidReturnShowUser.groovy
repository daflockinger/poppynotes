package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request { 
		method 'POST' 
		url '/api/v1/user-checks/auth'
		body(
			   authEmail:'sep@gmail.com'
		)
		headers { 
			contentType('application/json')
		}
	}
	response { 
		status 200 
		body([ 
			   id: 2,
			   name: "sepp",
			   recoveryEmail: "sep@gmx.net",
			   roles: ["AUTHOR"],
			   status: "ACTIVE",
			   cryptKey: "89768iksgd",
		])
		headers { 
			contentType('application/json')
		}
	}
}