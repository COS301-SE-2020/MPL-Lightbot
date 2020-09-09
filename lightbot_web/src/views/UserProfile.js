import React from "react";
import axios from "axios";
import Cookies from "universal-cookie";

// reactstrap components
import { Alert, Button, Card, CardHeader, CardBody, CardFooter, CardText, FormGroup, Form, Input, Row, Col } from "reactstrap";
const cookies = new Cookies();

export default class UserProfile extends React.Component {

	constructor(props) {

		super(props);
		
		this.state = {
            email: "",
            name: "",
            surname: "",
            privilage: "",
            changeSuccess: "",
			changeErrors: "",
			
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
	}

	componentDidMount() {
		var aresponse;

		axios
		 .post( "http://129.232.161.210:8000/user/loggedIn", {
			// .post( "http://localhost:8000/user/loggedIn", {
				User_email: cookies.get("Email")
			})
			.then(response => {
				aresponse = response.status;
				if (response.status === 200)
				{
					console.log(response)
					this.setState({
						email: response.data.User_email, 
						name: response.data.User_name,
						surname: response.data.User_surname
					});	
				}
			});

			if (aresponse === 200) {
				this.setState({ changeSuccess: "Registration Successful" });
		   } else {
			   // Set login error state as error
			   this.setState({ changeErrors: ""});
		   }

	}

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value,
        });
	}
	
	// HANDLE UPDATE REQUEST
	handleSubmit = async event => {
		event.preventDefault();

		//Reset response messages
		this.state.changeErrors = "";
		this.state.changeSuccess = "";

		const { email, name, surname } = this.state;

		await axios
			 .post( "http://129.232.161.210:8000/user/update-details", {
				// .post( "http://localhost:8000/user/update-details", {
				User_email: email,
				User_name: name,
				User_surname: surname,
			})
			.then(response => {
				console.log("Success ========>", response);
				// if (response.status === 200)
				// {
				// 	this.setState({ changeSuccess: "Update Successful" });
				// }
				
			})
			.catch(error => {
				// this.setState({ changeErrors: "Update Unsuccessful" });
			});

			
		
	};	

	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col md="8">
							<Card>
								<CardHeader>
									<h5 className="title">Edit Profile</h5>
								</CardHeader>
								<CardBody>
									<Form>
										<Row>
											<Col className="pr-md-1" md="5">
												<FormGroup>
													<label>User privilages</label>
													<Input defaultValue="Administrator" placeholder="Privilages" type="text" disabled="disabled"/>
												</FormGroup>
											</Col>
											<Col className="pl-md-1" md="4">
												<FormGroup>
												<label htmlFor="exampleInputEmail1">Email address</label>
                                                    <Input defaultValue={this.state.email} placeholder="Email" type="text" name="email" disabled="disabled"/>
												</FormGroup>
											</Col>
										</Row>
										<Row>
											<Col className="pr-md-1" md="6">
												<FormGroup>
													<label>First Name</label>
													<Input defaultValue={this.state.name} placeholder="Name" type="text" name="name" onChange={this.handleChange}/>
												</FormGroup>
											</Col>
											<Col className="pl-md-1" md="6">
												<FormGroup>
													<label>Last Name</label>
													<Input defaultValue={this.state.surname} placeholder="Surname" type="text" name="surname" onChange={this.handleChange}/>
												</FormGroup>
											</Col>
										</Row>
									</Form>
								</CardBody>
								<CardFooter>
									<Button className="btn-fill" color="primary" type="submit" onClick={this.handleSubmit} block>
										Save
									</Button>
								</CardFooter>
							</Card>
						</Col>
						<Col md="4">
							<Card className="card-user">
								<CardBody>
									<CardText />
									<div className="author">
										<div className="block block-one" />
										<div className="block block-two" />
										<div className="block block-three" />
										<div className="block block-four" />
										<a href="#pablo" onClick={e => e.preventDefault()}>
											<img alt="..." className="avatar" src={require("assets/img/Duncan.jpeg")} />
											<h5 className="title">{this.state.name + " " + this.state.surname}</h5>
											
										</a>
									</div>
								</CardBody>
							
							</Card>
						</Col>
					</Row>

					{this.state.changeErrors && (
					<Alert color="dark" style={MyStyles.Alrt}>
						{this.state.changeErrors}
					</Alert>
					)}

					{this.state.changeSuccess && (
					<Alert color="success" style={MyStyles.Alrt}>
						{this.state.changeSuccess}
					</Alert>
					)}
				</div>
			</>
		);
	}
}

const MyStyles = {
	Alrt: { 
		marginTop: "30px", 
		marginLeft: "40px", 
		marginRight: "40px" 
	},
}
