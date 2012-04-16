(doc) =>
  if (doc.type == 'transaction')
    emit(doc.to, doc.amount)
    emit(doc.from, -1 * doc.amount)
