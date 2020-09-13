import React, { useState } from "react";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Input, Form } from 'reactstrap';

const Modal1 = (props) => {
    const {
      className
    } = props;
  
    const [modal, setModal] = useState(false);
    const [unmountOnClose] = useState(true);
  
    const toggle = () => setModal(!modal);
  
    return (
        <div>
            <Form inline onSubmit={(e) => e.preventDefault()}>
                <Button color="white" onClick={toggle}>Add Forum Post</Button>
            </Form>
            <Modal isOpen={modal} toggle={toggle} className={className} unmountOnClose={unmountOnClose} >
                <ModalHeader toggle={toggle} style={MyStyles.heading1}>Add forum post:</ModalHeader>
                <ModalBody>
                    <Input type="textarea" placeholder="Title" rows={1} style={MyStyles.input1}/>
                    <Input type="textarea" placeholder="Message" rows={3} style={MyStyles.input1}/>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={toggle}>Submit</Button>{' '}
                    <Button color="secondary" onClick={toggle}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
}

const MyStyles = {
    input1: {
        fontSize: "13px",
        color: "black",
        paddingLeft: "15px",
        paddingBottom: "5px"
    },
    heading1: {
        fontSize: "30px",
        color: "black",
        paddingLeft: "25px",
    }
}

export default Modal1;
