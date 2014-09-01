//
//  WebViewController.h
//  FF-IOS-ScriptAuthTest
//
//  Created by David Wells on 2/17/13.
//  Copyright (c) 2013 FatFractal. All rights reserved.
//

#import <UIKit/UIKit.h>

@class WebViewController;

@protocol WebViewControllerDelegate <NSObject>

- (void)webViewController:(WebViewController *)webViewController didReceiveCallbackUriWithCode:(NSString *)callbackUriWithCode;
- (void)webViewControllerDidCancel:(WebViewController *)webViewController;

@end


@interface WebViewController : UIViewController <UIWebViewDelegate>

@property (weak, nonatomic) id<WebViewControllerDelegate> delegate;

@property (strong, nonatomic) NSString *scriptAuthService;
@property (strong, nonatomic) NSString *callbackUri;
@property (strong, nonatomic) NSString *authUri;

@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *cancelButton;

- (IBAction)cancel:(id)sender;

@end
