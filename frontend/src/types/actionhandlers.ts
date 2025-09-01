export interface CardActionHandlers<T> {
  onEdit: (item: T) => void;
  onDelete: (item: T) => void;
  onDetail: (item: T) => void;
}