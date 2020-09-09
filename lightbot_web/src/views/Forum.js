import React from "react";
import axios from "axios";
import Cookies from "universal-cookie";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Table, Row, Col } from "reactstrap";
const cookies = new Cookies();

export default class Forum extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
            title: "",
            message: "",
            creator: "",
			date: "",
			category: ""
            // changeSuccess: "",
			// changeErrors: "",
			
        };
        // this.handleSubmit = this.handleSubmit.bind(this);
        // this.handleChange = this.handleChange.bind(this);
	}

	componentDidMount() {
		axios
		 .post( "http://129.232.161.210:8000/data/forum", {
			// .post( "http://localhost:8000/data/forum", {
				User_email: cookies.get("Email")
			})
			.then(response => {
				if (response.status === 200)
				{
					// dataS = response.data;

					console.log(response)
					this.setState({
						title: response.data.title, 
						message: response.data.message,
						creator: response.data.creator,
						date: response.data.date,
						category: response.data.category
					});
					
				}
			});
			const { title, message, creator, date } = this.state;
	}

	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Bug Reports</b>
									</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th>Title</th>
												<th>Message</th>
												<th>Posted By</th>
												<th className="text-center">Date</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td defaultValue = {this.state.title} placeholder="Title" type="text" name="title">Manual Override</td>
												<td>Screen freezes on 3rd override</td>
												<td>Laura Strydom</td>
												<td className="text-center">04 August 2020</td>
											</tr>
											<tr>
												<td>Log Out Bug</td>
												<td>Times out on certain log outs</td>
												<td>Anthony Boekkooi</td>
												<td className="text-center">14 July 2020</td>
											</tr>
											
										</tbody>
									</Table>
								</CardBody>
							</Card>
						</Col>

						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Resolved Bug Reports</b>
									</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th>Title</th>
												<th>Message</th>
												<th>Posted By</th>
												<th className="text-center">Date</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>Manual Override Bug</td>
												<td>Freezing screen - bug fixed</td>
												<td>John Wick</td>
												<td className="text-center">02 August 2020</td>
											</tr>
											<tr>
												<td>Log Out Bug</td>
												<td>Time out fixed on log out function</td>
												<td>King Peterson</td>
												<td className="text-center">27 July 2020</td>
											</tr>
										</tbody>
									</Table>
								</CardBody>
							</Card>
						</Col>

						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Announcements</b>s
									</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th>Title</th>
												<th>Message</th>
												<th>Posted By</th>
												<th className="text-center">Date</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>System Upgrade</td>
												<td>Time for a system upgrade!</td>
												<td>King Peterson</td>
												<td className="text-center">19 August 2020</td>
											</tr>
											<tr>
												<td>System Down Time</td>
												<td>The system will be down on Tuesday 15/05/2020 for system fixes.</td>
												<td>Laura Strydom</td>
												<td className="text-center">29 July 2020</td>
											</tr>
										</tbody>
									</Table>
								</CardBody>
							</Card>
						</Col>
					</Row>
				</div>
			</>
		);
	}
}

