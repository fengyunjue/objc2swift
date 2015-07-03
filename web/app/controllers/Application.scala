package controllers

import play.api.mvc._

import org.objc2swift._

class Application extends Controller {

  private val defaultInput =
    """
      |@interface MyClass
      |
      |- (void)sayHello;
      |
      |@end
      |
      |@implementation MyClass
      |
      |- (void)sayHello{
      |    NSLog(@"Hello Swift, Goodbye Obj-C!");
      |}
      |
      |@end
      |
    """.stripMargin

  def index = Action { implicit request =>
    val form = request.body.asFormUrlEncoded
    val raw = form.flatMap(_.get("raw")).map(_(0) == "1").getOrElse(false)
    val input = form.flatMap(_.get("source")).map(_(0)).getOrElse(defaultInput)

    val converter = new ObjC2SwiftConverter(input)
    val result = converter.getResult

    if(!raw)
      Ok(views.html.index("objc2swift-web", input, result))
    else
      Ok(result).as(HTML)
  }
}
