import React from "react";
// import Cookies from "universal-cookie";
import { Link } from "react-router-dom";
import axios from "axios";

import { Alert, Button, FormGroup, Form, Input } from "reactstrap";


export default class RegisterView extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			name: "",
			surname: "",
			email: "",
			password: "",
			confirmpass: "",
			regErrors: "",
			regSuccess: "",
		};

		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleChange = this.handleChange.bind(this);
	}

	handleChange(event) {
		this.setState({
			[event.target.name]: event.target.value,
		});
	}

	// HANDLE REGISTER REQUEST
	handleSubmit = async event => {
		event.preventDefault();

		//Reset response messages
		this.state.regErrors = "";
		this.state.regSuccess = "";

		const { name, surname, email, password, confirmpass } = this.state;
		var aresponse;
		var bresponse;

		var createdUser = {
			User_name: name,
			User_surname: surname,
			User_email: email,
			User_password: password,
		  }
		
		if (password !== confirmpass)
		{
			this.setState({ regErrors: "The Passwords do not match."});
			return;
		}  

		await axios
			 .post( "http://129.232.161.210:8000/user/register", {
				// .post( "http://localhost:8000/user/register", {
				User_name: name,
				User_surname: surname,
				User_email: email,
				User_password: password,
			})
			.then(response => {
				console.log("Success ========>", response);
				aresponse = response.status;
				bresponse = response.data;
			})
			.catch(error => {
				// console.log("Error: Cannot connect to server", error);
			});

		if (aresponse === 200) {

		 	// this.setState({ regSuccess: "Registration Successful" });

			//Redirect the user to the login page
			window.location = "/admin/login";
		} else {
			// Set login error state as error
			// this.setState({ regErrors: ""});
			// console.log("Error Registering User");
		}
	};


	render() {
		return (
			<Form className="LoginPane">
				<img className="LoginLogo" alt="..." src={require("assets/img/LBlogo.png")} />

				<h1 className="LoginLabel" style={MyStyles.Hdr}>
					Sign Up
				</h1>

				<FormGroup style={MyStyles.Input1}>
					
					<Input
						type="text"
						name="name"
						id="idName"
						placeholder="Name"
						value={this.state.name}
						onChange={this.handleChange}
						style={MyStyles.Input2}
						required
					/>
				</FormGroup>

				<FormGroup style={MyStyles.Input1}>
					
					<Input
						type="text"
						name="surname"
						id="idSurname"
						placeholder="Surname"
						value={this.state.surname}
						onChange={this.handleChange}
						style={MyStyles.Input2}
						required
					/>
				</FormGroup>

				<FormGroup style={MyStyles.Input1}>
					
					<Input
						type="email"
						name="email"
						id="idEmail"
						placeholder="Email"
						value={this.state.email}
						onChange={this.handleChange}
						style={MyStyles.Input2}
						required
					/>
				</FormGroup>

				<FormGroup style={MyStyles.Input1}>
					
					<Input
						type="password"
						name="password"
						id="idPassword"
						placeholder="Password"
						value={this.state.password}
						onChange={this.handleChange}
						style={MyStyles.Input2}
						required
					/>
				</FormGroup>

				<FormGroup style={MyStyles.Input1}>
	
					<Input
						type="password"
						name="confirmpass"
						id="confirmpass"
						placeholder="Confirm Password"
						value={this.state.confirmpass}
						onChange={this.handleChange}
						style={MyStyles.Input2}
						required
					/>
				</FormGroup>

				{this.state.regErrors && (
					<Alert color="dark" style={MyStyles.Alrt}>
						{this.state.regErrors}
					</Alert>
				)}

				{this.state.regSuccess && (
					<Alert color="success" style={MyStyles.Alrt}>
						{this.state.regSuccess}
					</Alert>
				)}

				<FormGroup style={MyStyles.SBtn1}>
					<Button 
						style={MyStyles.SBtn2} 
						size="lg" 
						type="submit"
						onClick={this.handleSubmit}
						block
					>
						Sign Up
					</Button>
				</FormGroup>

				<Link to='/login' style={MyStyles.CreateAccountref}>
            		Back to login page
          		</Link>

				<img
            className='LoginGLogo'
            alt='...'
            src={require('../assets/img/traffic.png')}
            style={MyStyles.LoginGLogo}
          />

			</Form>
		);
	}
}

const MyStyles = {
	CreateAccountref: {
		float: 'center',
		color: 'white',
		fontSize: 20,
		paddingLeft: '40px',
		paddingRight: '40px',
		paddingBottom: '20px',
	  },
	LoginGLogo: {
		paddingTop: '20px',
		height: '80px',
		marginBottom: '20px'
	},
	SBtn1: { 
		paddingLeft: "40px", 
		paddingRight: "40px", 
		paddingTop: "10px" 
	},
	SBtn2: { 
		borderColor: "white", 
		backgroundColor: "transparent", 
		height: "50px", 
		borderWidth: "2" 
	},
	Input1: { 
		paddingLeft: "40px", 
		paddingRight: "40px" 
	},
	Input2: { 
		borderColor: "white", 
		backgroundColor: "transparent", 
		height: "50px" 
	}, 
	Hdr: { 
		marginBottom: 30 
	},
	Alrt: { 
		marginTop: "30px", 
		marginLeft: "40px", 
		marginRight: "40px" 
	}
}