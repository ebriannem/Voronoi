package voronoidiagram;

class Arc {
  //The focus of the parabola, if any
  Point focus;
  //The corrseponding edge, if any
  Edge edge;
  //The event where this arc disappears, if any
  Event disappearance;
  //The family of the arc
  Arc parent;
  Arc leftChild;
  Arc rightChild;
  //Whether the Arc is on the beach or not
  boolean onBeach;

  /**
   * Creates an arc on the beachline
   * @param focus the focus of the arc's parabola
   */
  Arc(Point focus) {
    this.focus = focus;
    this.onBeach = true;
  }

  /**
   * Creates an arc with an edge
   * @param edge the edge of the arc
   */
  Arc(Edge edge) {
    this.edge = edge;
    this.onBeach = false;
  }

  /**
   * Sets the left child and updates the new child's parent
   * @param a the child
   */
  void setLeftChild(Arc a) {
    this.leftChild = a;
    a.parent = this;
  }

  /**
   * Sets the right child and updates the new child's parent
   * @param a the child
   */
  void setRightChild(Arc a) {
    this.rightChild = a;
    a.parent = this;
  }

  /**
   * Adds an edge to this arc and removes it from the beachline
   * @param edge the edge to be added
   */
  void toEdge(Edge edge){
    this.onBeach = false;
    this.edge = edge;
  }

  /**
   * Finds the closest parent to the left of this.
   * @return the parent
   */
  Arc getLeftParent() {
    Arc parent = this.parent;
    Arc last = this;
    while (parent != null && parent.leftChild == last) {
      last = parent;
      parent = parent.parent;
    }
    return parent;
  }

  /**
   * Finds the closest parent to the right of this.
   * @return the parent
   */
  Arc getRightParent() {
    Arc parent = this.parent;
    Arc last = this;
    while (parent != null && parent.rightChild == last) {
      last = parent;
      parent = parent.parent;
    }
    return parent;
  }

  /**
   * Finds the closest left child on the beachline
   * @return the child
   */
  Arc getLeftChild() {
    Arc child = this.leftChild;
    while (child != null && !child.onBeach){
      child = child.rightChild;
    }
    return child;
  }

  /**
   * Finds the closest right child on the beachline
   * @return the child
   */
  Arc getRightChild() {
    Arc child = this.rightChild;
    while (child != null && !child.onBeach){
      child = child.leftChild;
    }
    return child;
  }
}
