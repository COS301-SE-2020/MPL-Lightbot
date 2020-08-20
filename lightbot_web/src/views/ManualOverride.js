import React from "react";

// reactstrap components
import { Card, CardHeader, CardBody, Row, Col, Button } from "reactstrap";
import { Dialog, applet } from '@material-ui/core';


export default class ManualOverride extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			dialogTitle: "",
			modal: "",
		};
		this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleSubmit = async event => {
		event.preventDefault();
		const { dialogTitle, modal } = this.state;

		// // var dangerousMarkup = {
		// // 	__html: 
	  	// 		<applet
		// 			// type="application/x-java-applet;version=1.6"
		// 			// // width="540"
		// 			// // height="480"
		// 			// codebase="./marvin"
		// 			// // archive="appletlaunch.jar"
		// 			// // code="chemaxon/marvin/applet/JMSketchLaunch"
		// 			// legacy_lifecycle={true}
		// 			// java-vm-args="-Djnlp.packEnabled=true -Xmx512m"
		// 			// codebase_lookup={false}>
		// 			code="MainClass.class" 
		// 			archive="/lightbot_web/src/Sim.jar" 
		// 			width="550" 
		// 			height="400">	
		// 		</applet>
		// }
	};

	render() {
		return (
			<>
				<div className="content">
					<Row>
						<Col md="12">
							<Card className="card-plain">
								<CardHeader>Manual Override</CardHeader>
								<CardBody>
									<div id="manual" className="manualO" style={MyStyles.Inside}>
									<Button className="btn-fill" color="primary" type="submit" onClick={this.handleSubmit} block>
										Launch Manual Override of Simulation
									</Button>
									</div>
								</CardBody>
							</Card>
						</Col>
					</Row>
				</div>
								
				{/* <div dangerouslySetInnerHTML={dangerousMarkup}> */}
				<Dialog
      				ref="dialogAction"
      				title={this.state.dialogTitle}
      				// actions={customAction}
      				actionFocus="submit"
      				modal={this.state.modal}
      				autoDetectWindowHeight={true}
      				autoScrollBodyContent={true}
					  // contentStyle={styles.dialogMarvin}
					>	
					<applet
						code="MainClass.class" 
						archive="/lightbot_web/src/Sim.jar" 
						width="550" 
						height="400">	
					</applet>
				</Dialog>
				{/* </div> */}
			</>
		);
	}
}

const MyStyles = {
	Inside: { 
		position: "relative", 
		overflow: "hidden" 
	},
}

