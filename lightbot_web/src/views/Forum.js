import React from "react";
import axios from "axios";
import Cookies from "universal-cookie";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Table, Row, Col, Button, Form, FormGroup, Input, Alert} from "reactstrap";
const cookies = new Cookies();


export default class Forum extends React.Component {
	constructor(props) {
		
		super(props);

		this.state = {
			title: "",
			message: "",
			creator: "",
			date: "",
			forums:[], 
			addSuccess: "",
			addErrors: "",
		};
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleChange = this.handleChange.bind(this);
		this.cancelSubmit = this.cancelSubmit.bind(this);
	}

	async componentDidMount() {	
		
		await axios.get("http://129.232.161.210:8000/data/forum", {
			//  axios.get("http://localhost:8000/data/forum", {
            headers: { 
                'Content-Type' : 'application/json' 
            },
		})
		.then((response) => {
			// console.log(response.data.success.data.ForumData)
			this.setState({forums: response.data.success.data.ForumData})
			this.state = {
					title: response.data.title,
				    message: response.data.message,
				    creator: response.data.creator,
					date: response.data.date,	
			}
		
			// console.log(response.data.success.data.ForumData)		
		})
	}

	handleChange(event) {
		this.setState({
			[event.target.name]: event.target.value,
		});
	}

	cancelSubmit(event){
		event.preventDefault();
		document.getElementById("title").value="";
		document.getElementById("message").value="";
	}

	handleSubmit = async event => {
		event.preventDefault();

		//Reset response messages
		this.state.addErrors = "";
		this.state.addSuccess = "";

		const { title, message } = this.state;

		await axios
			 .post( "http://129.232.161.210:8000/data/post-forum", {
			//  .post( "http://localhost:8000/data/post-forum", {
				title: title,
				message: message,
				creator: cookies.get("Email")
			})
			.then(response => {
				if (response.status === 200)
				{
					console.log(response)
					this.setState(prevState => ({
						forums: [...prevState.forums, {"title": response.data.success.data.ForumPost.title, 
														"message": response.data.success.data.ForumPost.message,
														"creator": response.data.success.data.ForumPost.creator.User_name + response.data.success.data.ForumPost.creator.User_surname,
														"date": response.data.success.data.ForumPost.date}]

					  }))
					this.setState({ addSuccess: "Post Added Successfully" });
				}
				else{
					this.setState({ addErrors: "Post Unsuccessful" });
				}
			});
	};

	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Discussion Board</b>
									</CardTitle>
										
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th>Title</th>
												<th>Message</th>
												<th>Posted By</th>
												<th>Date</th>
											</tr>
										</thead>
										<tbody>
											
											{this.state.forums.map((item, index) => (
												<tr>
													<td key={index}>{item.title}</td>
													<td key={index}>{item.message}</td>
													<td key={index}>{item.creator}</td>
													<td key={index}>{item.date}</td>
												</tr>
											))}
											
										</tbody>
									</Table>
								</CardBody>
							</Card>
						</Col>
					</Row>

					<Row>
						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Add Forum Post</b>
									</CardTitle>
			
								</CardHeader>
								<CardBody>
									<Form >
									<FormGroup >
											<Input
												type="text"
												name="title"
												id="title"
												placeholder="Title"
												value={this.state.title}
												onChange={this.handleChange}
												
												required
											/>
										</FormGroup>

										<FormGroup >
											<Input
												type="text"
												name="message"
												id="message"
												placeholder="Message"
												value={this.state.message}
												onChange={this.handleChange}
												
												required
											/>
										</FormGroup>

										<FormGroup >
											<Button
												style={MyStyles.Btn2}
												size="lg"
												type="submit"
												onClick={this.handleSubmit}
												block
											>
												Submit
											</Button>
										</FormGroup>

										<FormGroup >
											<Button
												style={MyStyles.Btn2}
												size="lg"
												type="cancel"
												onClick={this.cancelSubmit}
												block
											>
												Cancel
											</Button>
										</FormGroup>

									</Form>

									{this.state.addSuccess && (
										<Alert color="success" style={MyStyles.Alrt}>
											{this.state.addSuccess}
										</Alert>
									)}
									{this.state.addErrors && (
										<Alert color="danger" style={MyStyles.Alrt}>
											{this.state.addErrors}
										</Alert>
									)}
								</CardBody>
							</Card>
						</Col>
					</Row>
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
	Btn2: {
		width: "150px",
		height: "40px",
		paddingTop: "10px"
	}
}