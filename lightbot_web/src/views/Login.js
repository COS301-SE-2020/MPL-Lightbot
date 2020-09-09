import React from "react";
import Cookies from "universal-cookie";
import { Link } from "react-router-dom";
import axios from "axios";

import { Alert, Button, FormGroup, Form, Input } from "reactstrap";

const cookies = new Cookies();

export default class Login extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			email: "",
			password: "",
			loginErrors: "",
			loginSuccess: "",
		};

		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleChange = this.handleChange.bind(this);
	}

	handleChange(event) {
		//console.log(event.target.value);
		this.setState({
			[event.target.name]: event.target.value,
		});
	}

	// HANDLE LOGIN REQUEST
	handleSubmit = async event => {
		event.preventDefault();

		//Reset response messages
		this.state.loginErrors = "";
		this.state.loginSuccess = "";

		const { email, password } = this.state;

		var isValidCred = false;
		var sessionToken = "";
		await axios
			 .post( "http://129.232.161.210:8000/user/login", {
			//  .post( "http://localhost:8000/user/login", {
				User_email: email,
				User_password: password,
			})
			.then(response => {
				console.log("Success ========>", response);
				if (response.status === 200)
				{
					isValidCred = true;
					sessionToken = response.data;
				}
				// HANDLE RESPONSE FROM API HERE
			})
			.catch(error => {
				console.log("Error: Cannot connect to server", error);
			});

		if (isValidCred && sessionToken !== "") {
			//CALL TO API HERE WITH EMAIL AND PASSWORD

			// If login success: Set the JWT from API in cookies
			// and set the logged in user email address also

			this.setState({ loginSuccess: "Login Successful" });
			cookies.set("JWT", sessionToken);
			cookies.set("Email",email);

			//Redirect the user to the dashboard
			window.location = "/admin/dashboard";
		} else {
			// Set login error state as error
			this.setState({ loginErrors: "Invalid Credentials" });
		}
	};

	render() {
		return (
			// All styles here are localized , move to scss in future
			<Form className="LoginPane">
				<img className="LoginLogo" alt="..." src={require("assets/img/LBlogo.png")} />

				<h4 style={MyStyles.Hdr}>AN ENHANCED ADAPTIVE TRAFFIC OPTIMIZATION SOLUTION WITH REINFORCEMENT LEARNING</h4>
				
				{/* <h1 className="LoginLabel" style={MyStyles.Hdr}>
					LOGIN
				</h1> */}
				<h2 style={MyStyles.LoginLabel2}>Welcome! Please Log In</h2>

				<FormGroup style={MyStyles.Linput1}>
		
					<Input
						type="email"
						name="email"
						id="idEmail"
						placeholder="Email"
						value={this.state.email}
						onChange={this.handleChange}
						style={MyStyles.Linput2}
						required
					/>
				</FormGroup>

				<FormGroup style={MyStyles.Linput1}>
					<Input
						type="password"
						name="password"
						id="idPassword"
						placeholder="Password"
						value={this.state.password}
						onChange={this.handleChange}
						style={MyStyles.Linput2}
						required
					/>
				</FormGroup>

				{this.state.loginErrors && (
					<Alert color="dark" style={MyStyles.Alrt}>
						{this.state.loginErrors}
					</Alert>
				)}

				{this.state.loginSuccess && (
					<Alert color="success" style={MyStyles.Alrt}>
						{this.state.loginSuccess}
					</Alert>
				)}

				<FormGroup style={MyStyles.Btn1}>
					<Button
						style={MyStyles.Btn2}
						size="lg"
						type="submit"
						onClick={this.handleSubmit}
						block
					>
						Log In
					</Button>
				</FormGroup>

		  <Link to='/register' style={MyStyles.CreateAccountref}>
            Create Account
          </Link>

          <Link to='/forgot-password' style={MyStyles.CreateAccountref}>
            Forgot password?
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
	Alrt: { 
		marginTop: "30px", 
		marginLeft: "40px", 
		marginRight: "40px" 
	},
	Btn1: { 
		paddingLeft: "40px", 
		paddingRight: "40px", 
		paddingTop: "40px", 
		paddingBottom: "20px" 
	},
	Btn2: { 
		borderColor: "white", 
		backgroundColor: "transparent", 
		height: "50px", 
		borderWidth: "2" 
	},
	Linput1: { 
		paddingLeft: "40px", 
		paddingRight: "40px" 
	},
	Linput2: { 
		borderColor: "white", 
		backgroundColor: "transparent", 
		height: "50px", 
		fontSize: "18px" 
	},
	// Hdr: { 
	// 	marginBottom: 20 
	// }
}
