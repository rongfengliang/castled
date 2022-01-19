import { UserDTO } from "./UserDTO";

export interface TeamDTO {
  teamId: number;
  teamName: string;
  teamTier: string;
  activeMembers: Array<UserDTO>;
  pendingInvitees: Array<UserDTO>;
}
