import React from "react";
// nodejs library that concatenates classes
import classNames from "classnames";
// react plugin used to create charts
import { Line } from "react-chartjs-2";
import axios from "axios";

// reactstrap components
import {
	Button,
	ButtonGroup,
	Card,
	CardHeader,
	CardBody,
	CardTitle,
	// Input,
	Table,
	Row,
	Col,
} from "reactstrap";

// core components
import { chartExample1 } from "variables/charts.js";

//API Server IP: 129.232.161.210
export default class Dashboard extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			number: "",
			systemState: "",
			controller: "",
			controllerState: "",
			overview:[],
				// changeSuccess: "",
				// changeErrors: "",
		};
		 
		axios.get("http://129.232.161.210:8000/data/graph", {
			//  axios.get("http://localhost:8000/data/graph", {
            headers: { 
                'Content-Type' : 'application/json' 
            },
	  	})
	  	.then((response) => {
			//   console.log(response.data.success.data);
			let dataArr =[];
			let items = response.data.success.data.GraphData;
			for (let i = 0; i < items.length; ++i) {
				dataArr.push(items[i].Total_sim);
			}
			chartExample1.setData(dataArr);
		  });

		this.state = {
			bigChartData: "data1",
		};
	}

	async componentDidMount() {	
		
		await axios.get("http://129.232.161.210:8000/data/state", {
			//  axios.get("http://localhost:8000/data/state", {
            headers: { 
                'Content-Type' : 'application/json' 
            },
		})
		.then((response) => {
			// console.log(response.data.success.data.StateData)
			
			this.setState({overview: response.data.success.data.StateData})
			this.state = {
				number: response.data.number,
				systemState: response.data.systemState,
				controller: response.data.controller,
				controllerState: response.data.controllerState,	
			}
			console.log(this.state.overview)
			// console.log(response)		
		})
	}

	setBgChartData = name => {
		this.setState({
			bigChartData: name,
		});
	};

	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col xs="12">
							<Card className="card-chart">
								<CardHeader>
									<Row>
										<Col className="text-left" sm="6">
											<h5 className="card-category">Total Simulations</h5>
											<CardTitle tag="h2">Performance</CardTitle>
										</Col>
										<Col sm="6">
											<ButtonGroup className="btn-group-toggle float-right" data-toggle="buttons">
												<Button
													tag="label"
													className={classNames("btn-simple", {
														active: this.state.bigChartData === "data1",
													})}
													color="info"
													id="0"
													size="sm"
													onClick={() => this.setBgChartData("data1")}
												>
													<input defaultChecked className="d-none" name="options" type="radio" />
													<span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">Sessions</span>
													<span className="d-block d-sm-none">
														<i className="tim-icons icon-single-02" />
													</span>
												</Button>
											</ButtonGroup>
										</Col>
									</Row>
								</CardHeader>
								<CardBody>
									<div className="chart-area">
										<Line data={chartExample1[this.state.bigChartData]} options={chartExample1.options} />
									</div>
								</CardBody>
							</Card>
						</Col>
					</Row>
					<Row>
						<Col lg="6" md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">System Overview</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th>Nr.</th>
												<th>State of System</th>
												<th>Active Traffic Controller</th>
												<th>Controller State</th>
											</tr>
									</thead>
										<tbody>
											{/* {this.state.overview.map((item, index) => (
												<tr>
													<td key={index}>{item.number}</td>
													<td key={index}>{item.systemState}</td> 
													<td key={index}>{item.controller}</td>
													<td key={index}>{item.controllerState}</td>
												</tr>
											))} */}
											
										</tbody>
									</Table>
								</CardBody>
							</Card>
						</Col>						

						<Col lg="6" md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">Simulation Statistics</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
									<thead className="text-primary">
											<tr>
												<th className="text-center">Week</th>
												<th>Average Idle Time of Cars</th>
												<th>Average Amount of Cars Passing Through</th>
												<th>Average Amount of Time Elapsed per Cycle</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<th className="text-center">3</th>
												<td>5.78 sec</td>
												<td>17 cars</td>
												<td>47.31 sec</td>
											</tr>
											<tr>
												<th className="text-center">2</th>
												<td>7.02 sec</td>
												<td>24 cars</td>
												<td>77.56 sec</td>
											</tr>
											<tr>
												<th className="text-center">1</th>
												<td>6.30 sec</td>
												<td>19 cars</td>
												<td>53.10 sec</td>
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