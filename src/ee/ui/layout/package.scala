package ee.ui

package object layout {
  type Width = Double
  type Height = Double
  type NodeWidth = Width
  type NodeHeight = Height
  type Size = (Width, Height)
  type ParentRelatedSizeNode = Node with ParentRelatedSize
}