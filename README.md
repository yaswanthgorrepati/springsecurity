# springsecurity
user authentication and authorization  application

# End point 1:
POST: http://localhost:8080/authenticate
HEADERS:
      Content-Type:application/json
BODY: 
     {
	    "username":"example@gmail.com",
	    "password":"123456"
     }
     
# End point 2:
PUT: http://localhost:8080/forgotpassword
HEADERS:
      Content-Type:application/json
      Authorization:Bearer {token}
BODY: 
     {
	    "username":"example@gmail.com"
     } 
A mail will be sent to the provided mail if it's been registered in the system earlier.     
 
# End point 3:
POST: http://localhost:8080/registration
HEADERS:
      Content-Type:application/json
BODY: 
     {
	    "username":"example@gmail.com",
	    "password":"123456"
     } 
 A successfull registration mail will be sent to the provided mail.     
     
# End point 4:
PUT: http://localhost:8080/passwordChange
HEADERS:
      Content-Type:application/json
      Authorization:Bearer {token}
BODY: 
     {
      "password":"newPassword"
     }     
 
