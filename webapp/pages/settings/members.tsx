import { TeamDTO } from "@/app/common/dtos/TeamDTO";
import settingService from "@/app/services/settingService";
import React, { useEffect, useState } from "react";
import {
  Button,
  InputGroup,
  Table,
  Modal,
  FormControl,
  ToastContainer,
  Toast,
} from "react-bootstrap";
import { IconUserPlus, IconTrash, IconX } from "@tabler/icons";

const MembersTab = () => {
  const [temaMembers, setTeamMembers] = useState<TeamDTO | undefined | null>();
  const [email, setEmail] = useState<string>("");
  const [isValid, setIsValid] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [message, setMessage] = useState<string>("");
  const [showA, setShowA] = useState<boolean>(false);
  useEffect(() => {
    settingService
      .teamMember()
      .then(({ data }) => {
        console.log(data);
        setTeamMembers(data as any);
      })
      .catch(() => {
        setTeamMembers(null);
      });
  });
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const sendInvite = () => {
    if (!email) {
      setIsValid(true);
      return;
    }
    settingService
      .inviteMember([{ email }])
      .then(() => {
        setMessage("Invitation sent successfully");
        setShowA(true);
        handleClose();
      })
      .catch((err: any) => {
        console.log(err);
        setErrorMessage(err.message);
        setIsValid(true);
      });
  };
  const resendInvitation = (email: string) => {
    settingService
      .resendInvitation([{ email }])
      .then(() => {
        setMessage("Resent invitation");
        setShowA(true);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };
  const cancelInvitation = (email: string) => {
    settingService
      .cancelInvitation([email])
      .then(() => {
        setMessage("Cancelled invitation");
        setShowA(true);
      })
      .catch((err: any) => {
        console.log(err);
        // setIsValid(true);
      });
  };
  const removeMember = (email: string) => {
    settingService
      .removeMember([email])
      .then(() => {
        setMessage("Removed member");
        setShowA(true);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const handleChange = (event: any) => {
    setEmail(event.target.value);
    setIsValid(false);
    setErrorMessage("");
  };
  const toggleShowA = () => setShowA(!showA);

  return (
    <div>
      {temaMembers && temaMembers!.pendingInvitees.length !== 0 && (
        <>
          <h3 className="mb-3 mt-4 font-weight-bold">1 pending invite</h3>
          <Table hover>
            <thead>
              <tr>
                <th>Email</th>
                <th>Resend</th>
                <th>Cancel Invitation</th>
              </tr>
            </thead>
            <tbody>
              {temaMembers !== undefined &&
                temaMembers!.pendingInvitees.map((fieldMapping, i) => (
                  <tr key={i}>
                    <td>{fieldMapping.email}</td>
                    <td>
                      <Button
                        variant="link"
                        onClick={() => resendInvitation(fieldMapping.email)}
                      >
                        Resend invitation
                      </Button>
                    </td>
                    <td>
                      <Button
                        variant="warning"
                        onClick={() => cancelInvitation(fieldMapping.email)}
                      >
                        Cancel invitation
                      </Button>
                    </td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </>
      )}
      <div className="d-flex justify-content-between">
        <h3 className="mb-1 mt-4 font-weight-bold">Members</h3>
        <div className="mt-3 mb-2">
          <Button variant="outline-primary" size="sm" onClick={handleShow}>
            <span>
              <IconUserPlus size={14} className="sidebar-icon" />
              &nbsp; Invite team member
            </span>
          </Button>
        </div>
      </div>
      <Table hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email Address</th>
            <th>Role</th>
            <th>Member Since</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {temaMembers !== undefined &&
            temaMembers?.activeMembers.map((fieldMapping, i) => (
              <tr key={i}>
                <td>{fieldMapping.name}</td>
                <td>{fieldMapping.email}</td>
                <td>{fieldMapping.role}</td>
                <td>{fieldMapping.createdTs}</td>
                <td>
                  <IconTrash
                    size={18}
                    className="sidebar-icon"
                    onClick={() => removeMember(fieldMapping.email)}
                  />
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
      <Modal show={show} onHide={handleClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>Invite team member</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <InputGroup className="mb-3">
            <FormControl
              type="email"
              isInvalid={!!isValid}
              placeholder="Email address"
              onChange={handleChange}
            />
            <FormControl.Feedback type="invalid">
              {errorMessage || "Enter a valid email address"}
            </FormControl.Feedback>
          </InputGroup>
          {/* <p>
            New users will be able to create workspaces and invite other team
            members.
          </p> */}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="outline-danger" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" onClick={sendInvite}>
            Send invitation
          </Button>
        </Modal.Footer>
      </Modal>

      <ToastContainer className="p-3" position="top-center">
        <Toast show={showA} onClose={toggleShowA}>
          <Toast.Body>
            <div className="d-flex justify-content-between">
              {message}
              <IconX
                size={18}
                className="sidebar-icon"
                onClick={toggleShowA}
              ></IconX>
            </div>
          </Toast.Body>
        </Toast>
      </ToastContainer>
    </div>
  );
};

export default MembersTab;
