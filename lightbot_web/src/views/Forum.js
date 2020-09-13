import React from "react";
import axios from "axios";
// import Cookies from "universal-cookie";
import Modal from "./Modal";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Table, Row, Col} from "reactstrap";
// const cookies = new Cookies();


export default class Forum extends React.Component {
	constructor(props) {
		
		super(props);

		this.state = {
			title: "",
			message: "",
			creator: "",
			date: "",
			forums:[]
			// changeSuccess: "",
			// changeErrors: "",
		};
	}

	async componentDidMount() {	
		
		await axios.get("http://129.232.161.210:8000/data/forum", {
			//  axios.get("http://localhost:8000/data/forum", {
            headers: { 
                'Content-Type' : 'application/json' 
            },
		})
		.then((response) => {
			// console.log(response)
			this.setState({forums: response.data.success.data.ForumData})
			this.state = {
					title: response.data.title,
				    message: response.data.message,
				    creator: response.data.creator,
					date: response.data.date,	
			}
			
			console.log(response)		
		})
	}

	state = { show: false };

	showModal = () => {
		this.setState({ show: true });
	};

	hideModal = () => {
		this.setState({ show: false });
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
										<Modal show={this.state.show} handleClose={this.hideModal}/>
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
				</div>
			</>
		);
	}
}

// const MyStyles = {
// 	modalStyle: {
// 		position: 'fixed',
// 		top: '0',
// 		left: '0',
// 		width: '100%',
// 		height: '100%',
// 		background: 'rgba(0, 0, 0, 0.6)'
// 	  }
// }