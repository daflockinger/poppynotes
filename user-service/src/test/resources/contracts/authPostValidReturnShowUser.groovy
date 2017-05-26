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
			   name: "sepp",
			   recoveryEmail: "sep@gmx.net",
			   roles: ["AUTHOR"],
			   status: "ACTIVE",
		])
		headers { 
			contentType('application/json')
		}
	}
}