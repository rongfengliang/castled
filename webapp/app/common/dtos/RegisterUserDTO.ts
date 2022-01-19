import { AppCluster } from "../enums/AppCluster";

export interface RegisterUserDTO {
  firstName: string;
  lastName: string;
  token: string;
  password: string;
  clusterLocation: AppCluster;
}
