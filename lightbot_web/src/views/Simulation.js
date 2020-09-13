import React from "react";

// reactstrap components
import { Card, CardHeader, CardBody, Row, Col, Button, Table, CardTitle} from "reactstrap";

export default class Simulation extends React.Component {
	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col md="12">
						<Card className="card-plain" >
								<CardHeader>Simulation</CardHeader>
								<CardBody>
									<div id="manual" className="manualO" style={MyStyles.Inside}>
									<a href={"http://129.232.161.210:8000/resources/Sim.jar"} target="_blank" rel="noopener noreferrer" download>
										{/* <a href={"http://localhost:8000/resources/Sim.jar"} target="_blank" rel="noopener noreferrer" download> */}
											<Button className="btn-fill" color="primary" type="submit" block>
												Download Simulator Desktop Application
											</Button>
										</a>
									</div>
								</CardBody>
							</Card>
						</Col>
					</Row>
					<Row>
						<Col md="12">
							<Card>
								<CardHeader>
									<CardTitle tag="h4">
										<b>Simulation Information</b>
									</CardTitle>
								</CardHeader>
								<CardBody>
									<Table className="tablesorter" responsive>
										<tbody>
											<tr>
												<td>You can download the Simulator Desktop Application by clicking on the above button. 
													Your download will start automatically. This will download an executable .jar file, which you can run on your local machine.
													No additional installation for this file is 
												</td>
												
											</tr>
											<tr>
												<td>*Please Note: To be able to run this application, you will need to install Java 8 on your local machine. 
													The link to download Java 8 can be found here: <a href="https://java.com/en/download/">https://java.com/en/download/</a>
												</td>
											</tr>
											<tr>
												<td>For more information about the simulator and how it works, see section 6 in the user manual.
													All relevant information can be found there. 
												</td>
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

const MyStyles = {
	cardH: { 
		paddingBottom: "40px"
	},
	backG: {
		backgroundColor: "white",
	},
	Inside: { 
		position: "relative", 
		overflow: "hidden" 
	},
}
