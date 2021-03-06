package io.github.vletard.analogy.sequence;

public class EquationReadingHead<E, S extends Sequence<E>>{
  private final SequenceEquation<E, S> equation;
  private final int a, b, c;
  private final Factorization<E, S> factorization;

  @Override
  public String toString(){
    return this.factorization.toString();
  }

  public EquationReadingHead(SequenceEquation<E, S> p){
    this.equation = p;
    this.a = 0;
    this.b = 0;
    this.c = 0;
    this.factorization = new Factorization<E, S>(equation.getRebuilder());
  }

  private E getB(){
    return this.equation.b.get(this.b);
  }

  private E getC(){
    return this.equation.c.get(this.c);
  }

  private EquationReadingHead(EquationReadingHead<E, S> eqn, Step step) throws ImpossibleStepException{
    if (!eqn.canStep(step))
      throw new ImpossibleStepException();
    this.equation = eqn.equation;
    int a = eqn.a;
    int b = eqn.b;
    int c = eqn.c;
    switch (step){
    case AB : this.factorization = eqn.factorization.extendListB(false, eqn.getB());
    a += 1;
    b += 1;
    break;
    case AC : this.factorization = eqn.factorization.extendListC(true, eqn.getC());
    a += 1;
    c += 1;
    break;
    case CD : this.factorization = eqn.factorization.extendListC(false, eqn.getC());
    c += 1;
    break;
    case BD : this.factorization = eqn.factorization.extendListB(true, eqn.getB());
    b += 1;
    break;
    default: throw new IllegalArgumentException("A new reading head can only be created with a defined step.");
    }
    this.a = a;
    this.b = b;
    this.c = c;
  }

  /**
   * Checks whether a given {@link Step} is possible for the current state of this reading head.
   * @param step the Step to be tested
   * @return true if the current state allows the step, false otherwise
   */
  public boolean canStep(Step step){
    switch (step){
    case AB : return (a < equation.a.size() && b < equation.b.size() && equation.a.get(a).equals(equation.b.get(b)));
    case AC : return (a < equation.a.size() && c < equation.c.size() && equation.a.get(a).equals(equation.c.get(c)));
    case CD : return (c < equation.c.size());
    case BD : return (b < equation.b.size());
    default : return false;
    }
  }

  /**
   * Checks whether this EquationReadingHead has reached the sink of the matrix. If true,
   * the factorization in this EquationReadingHead must contain the solution to the Equation.
   * See the getFactors() method. 
   * @return true if the progress in the matrix has finished.
   */
  public boolean isFinished(){
    return (a == equation.a.size() && b == equation.b.size() && c == equation.c.size());
  }

  /**
   * Returns the degree of this EquationReadingHead for its current state.
   * If isFinished() is false, the returned degree is a lower bound for the degree of
   * a potential solution.
   * @return The current degree of this EquationReadingHead.
   */
  public int getCurrentDegree() {
    return this.factorization.degree();
  }

  /**
   * Returns the factorization corresponding to this EquationReadingHead, for its current state.
   * It contains the solution built up to this state.
   * @return the factorization (list of {@link Factor}) of this EquationReadingHead.
   */
  public Factorization<E, S> getFactorization() {
    return this.factorization;
  }

  /**
   * Creates and return a new {@link EquationReadingHead} obtained by performing
   * the specified {@link Step}.
   * @param step Step to apply on the current reading head.
   * @param fastForward Whether to automatically repeat the same step as far as possible (keeping the same degree).  
   * @return A new reading head after performing the given step.
   * @throws ImpossibleStepException If the step cannot be applied.
   */
  public EquationReadingHead<E, S> makeStep(Step step, boolean fastForward) throws ImpossibleStepException {
    EquationReadingHead<E, S> newHead = new EquationReadingHead<E, S>(this, step);
    if (fastForward) {
      while (newHead.canStep(step) && (step != Step.BD || !newHead.canStep(Step.AB)) && (step != Step.CD || !newHead.canStep(Step.AC)) ) {
        EquationReadingHead<E, S> fastForwardHead = newHead.makeStep(step, fastForward);
        if (fastForwardHead.getCurrentDegree() > newHead.getCurrentDegree())
          break;
        else
          newHead = fastForwardHead;
      }
    }
    return newHead;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + a;
    result = prime * result + b;
    result = prime * result + c;
    result = prime * result + ((equation == null) ? 0 : equation.hashCode());
    result = prime * result + ((factorization == null) ? 0 : factorization.hashCode());
    return result;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EquationReadingHead other = (EquationReadingHead) obj;
    if (a != other.a)
      return false;
    if (b != other.b)
      return false;
    if (c != other.c)
      return false;
    if (equation == null) {
      if (other.equation != null)
        return false;
    } else if (!equation.equals(other.equation))
      return false;
    if (factorization == null) {
      if (other.factorization != null)
        return false;
    } else if (!factorization.equals(other.factorization))
      return false;
    return true;
  }
}
