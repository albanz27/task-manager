import { CommentResponseDTO } from "./comment.model";

export interface TaskAssignmentDTO {
  username: string;
  userFullName: string;
  assignmentDate: string;
  workedHours: number;
}

export interface TaskResponseDTO {
  id: number;
  title: string;
  description: string;
  status: 'BACKLOG' | 'IN_PROGRESS' | 'COMPLETED';
  totalHours: number;
  assignments: TaskAssignmentDTO[];
  comments: CommentResponseDTO[];
}