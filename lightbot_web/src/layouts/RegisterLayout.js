import React from "react";
import { Route } from "react-router-dom";
// javascript plugin used to create scrollbars on windows
import PerfectScrollbar from "perfect-scrollbar";

// core components
// import AdminNavbar from "components/Navbars/AdminNavbar.js";
// import Footer from "components/Footer/Footer.js";
// import Sidebar from "components/Sidebar/Sidebar.js";
// import Login from "views/Login.js";
import Register from "views/Register.js";

import routes from "routes.js";
// import logo from "assets/img/logo.png";
import Cookies from "universal-cookie";

import Background from "../assets/img/back.jpg";

var ps;
const cookies = new Cookies();

class RegisterLayout extends React.Component {
	constructor(props) {
		// VALIDATE JWT HERE (CHECK IF USER IS LOGGED IN)
		if (cookies.get("JWT")) {
			// REDIRECT TO LOGIN IF NOT
			window.location = "/admin/dashboard";
			return;
		}

		super(props);
		this.state = {
			backgroundColor: "black",
			sidebarOpened: document.documentElement.className.indexOf("nav-open") !== -1,
		};
	}
	componentDidMount() {
		if (navigator.platform.indexOf("Win") > -1) {
			document.documentElement.className += " perfect-scrollbar-on";
			document.documentElement.classList.remove("perfect-scrollbar-off");
			ps = new PerfectScrollbar(this.refs.mainPanel, { suppressScrollX: true });
			let tables = document.querySelectorAll(".table-responsive");
			for (let i = 0; i < tables.length; i++) {
				ps = new PerfectScrollbar(tables[i]);
			}
		}
	}
	componentWillUnmount() {
		if (navigator.platform.indexOf("Win") > -1) {
			ps.destroy();
			document.documentElement.className += " perfect-scrollbar-off";
			document.documentElement.classList.remove("perfect-scrollbar-on");
		}
	}
	componentDidUpdate(e) {
		if (e.history.action === "PUSH") {
			if (navigator.platform.indexOf("Win") > -1) {
				let tables = document.querySelectorAll(".table-responsive");
				for (let i = 0; i < tables.length; i++) {
					ps = new PerfectScrollbar(tables[i]);
				}
			}
			document.documentElement.scrollTop = 0;
			document.scrollingElement.scrollTop = 0;
			this.refs.mainPanel.scrollTop = 0;
		}
	}
	// this function opens and closes the sidebar on small devices
	toggleSidebar = () => {
		document.documentElement.classList.toggle("nav-open");
		this.setState({ sidebarOpened: !this.state.sidebarOpened });
	};

	getRoutes = routes => {
		return <Route exact path="/register" component={Register} />;
	};
	handleBgClick = color => {
		this.setState({ backgroundColor: color });
	};
	getBrandText = path => {
		for (let i = 0; i < routes.length; i++) {
			if (this.props.location.pathname.indexOf(routes[i].layout + routes[i].path) !== -1) {
				return routes[i].name;
			}
		}
		return "Welcome to LightBox";
	};
	render() {
		return (
			<>
				<div className="wrapper">
					{/* background image example styling below  */}
					<div
						className="main-panel"
						ref="mainPanel"
						style={MyStyles.backg}
					>
						{/* Login component is rendered here */}
						{this.getRoutes(routes)}
					</div>
				</div>
			</>
		);
	}
}


const MyStyles = {
	backg: {
		backgroundImage: "url(" + Background + ") ",
		backgroundRepeat: "no-repeat",
		backgroundPosition: "cover",
		backgroundPosition: "center",
		backgroundSize: "cover",
	}

}
export default RegisterLayout;
