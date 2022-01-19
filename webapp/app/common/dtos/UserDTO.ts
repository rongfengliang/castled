import { UserRole } from "./UserRole";

export interface UserDTO {
  email: string;
  name?: string;
  role?: UserRole;
  id?: string;
  createdTs?: number;
  firstSyncedTs?: number;
  team?: any;
}
