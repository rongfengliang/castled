import http from "@/app/services/http";
import { AxiosResponse } from "axios";
import { UserDTO } from "../common/dtos/UserDTO";

export default {
  teamMember: (): Promise<AxiosResponse<void>> => {
    return http.get("/v1/users/team-members");
  },
  inviteMember: (data: UserDTO[]): Promise<AxiosResponse<void>> => {
    return http.post("/v1/users/invite-member", { inviteeDetails: data });
  },
  resendInvitation: (data: UserDTO[]): Promise<AxiosResponse<void>> => {
    return http.post("/v1/users/resend-invitation", { inviteeDetails: data });
  },
  cancelInvitation: (email: string[]): Promise<AxiosResponse<void>> => {
    return http.post("/v1/users/cancel-invitation", { emailList: email });
  },
  removeMember: (email: string[]): Promise<AxiosResponse<void>> => {
    return http.post("/v1/users/remove-member", { emailList: email });
  },
};
